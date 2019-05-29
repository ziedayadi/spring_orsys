package com.acme.ex1;

import java.beans.Introspector;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.acme.ex1.repository.impl.FoxMovieRepositoryImpl;
import com.acme.ex1.repository.impl.WarnerMovieRepositoryImpl;
import com.acme.ex1.service.MovieService;
import com.acme.ex1.service.impl.MovieServiceImpl;
import com.acme.ex1.service.impl.SuperMovieServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import com.acme.ex1.model.Movie;
import com.acme.ex1.repository.MovieRepository;

public class MyFactory {

	// imaginons comment Spring découvre nos classes et méthodes annotées
	public static void main(String[] args) throws Throwable {

		java.lang.Class<?> configClass = ApplicationConfig.class;
		Object config = configClass.getConstructor().newInstance();

		Map<String, Object> beans = new HashMap<String, Object>();

		if (configClass.isAnnotationPresent(ComponentScan.class)) {
			Class<?>[] allClasses = new Class<?>[] {
					Movie.class,
					MovieRepository.class,
					FoxMovieRepositoryImpl.class,
					WarnerMovieRepositoryImpl.class,
					MovieService.class,
					MovieServiceImpl.class,
					SuperMovieServiceImpl.class
			};

			// en réalité Spring construit un graph de dépendance pour savoir par quels b
			List<Class<?>> componentClasses = Stream.of(allClasses).filter(clazz -> {
				if (clazz.isAnnotationPresent(Component.class)) {
					return true;
				}
				return Stream.of(clazz.getAnnotations())
						.anyMatch(a -> a.annotationType().isAnnotationPresent(Component.class));
			}).collect(Collectors.toList());

			for (Class<?> clazz : componentClasses) {
				if (clazz.isAnnotationPresent(Component.class) || Stream.of(clazz.getAnnotations())
						.anyMatch(a -> a.annotationType().isAnnotationPresent(Component.class))) {
					java.lang.reflect.Constructor<?> ctor = clazz.getConstructors()[0];
					// constructeur sans argument
					if (ctor.getParameterCount() == 0) {
						Object bean = ctor.newInstance();
						System.out.println(bean);
						beans.put(Introspector.decapitalize(clazz.getSimpleName()), bean);
					} else {
						// constructeur avec arguments
						Object[] argsForCtor = new Object[ctor.getParameterCount()];
						// pour chaque paramètre du constructeur
						for (int i = 0; i < ctor.getParameterCount(); i++) {
							// découverte du type du paramètre
							java.lang.reflect.Parameter p = ctor.getParameters()[i];
							Class<?> paramType = p.getType();
							if (Collection.class.isAssignableFrom(paramType)) {
								// si le paramètre est une collection typée (exemple : Set<MovieDao>)

								// accès au type générique (dans notre exemple : MovieDao)
								Type genericType = ((ParameterizedType) p.getParameterizedType())
										.getActualTypeArguments()[0];
								// cast en (Class<?>) pour pouvoir ensuite invoquer la méthode isAssignableFrom
								Class<?> genericTypeAsClass = (Class<?>) genericType;

								// recherche de tous les beans candidats à l'injection (ici : ceux de type
								// MovieDao)
								List<Object> candidates = beans.values().stream()
										.filter(bean -> genericTypeAsClass.isAssignableFrom(bean.getClass()))
										.collect(Collectors.toList());

								if (candidates.isEmpty()) {
									throw new Exception("could not inject anything for parameter " + i
											+ " of constructor " + ctor.getName());
								}

								if (paramType == Set.class) {
									argsForCtor[i] = new HashSet<>(candidates);
								} else if (paramType == List.class) {
									argsForCtor[i] = new ArrayList<>(candidates);
								}
							} else {
								// recherche quel(s) autre(s) bean(s) pourrai(en)t être injecté(s)
								List<Object> candidates = beans.values().stream()
										.filter(b -> paramType.isAssignableFrom(b.getClass()))
										.collect(Collectors.toList());

								if (candidates.isEmpty()) {
									throw new Exception("could not inject anything for parameter " + i
											+ " of constructor " + ctor.getName());
								} else if (candidates.size() > 1) {
									throw new Exception("expected single matching bean but found 2");
								}
								argsForCtor[i] = candidates.get(0);
							}
						}
						Object bean = ctor.newInstance(argsForCtor);
						beans.put(Introspector.decapitalize(clazz.getSimpleName()), bean);
					}
				}
			}
		}

		for (java.lang.reflect.Method method : configClass.getMethods()) {
			if (method.isAnnotationPresent(Bean.class)) {
				System.out.println(method);
				Object[] argsForMethod = new Object[method.getParameterCount()];
				for (int i = 0; i < method.getParameterCount(); i++) {
					Parameter p = method.getParameters()[i];
					Class<?> paramType = p.getType();
					if (Collection.class.isAssignableFrom(paramType)) {
						// TODO
					}

					List<Object> candidates = beans.values().stream()
							.filter(x -> paramType.isAssignableFrom(x.getClass())).collect(Collectors.toList());
					if (candidates.isEmpty()) {
						throw new Exception(
								"could not inject anything for parameter " + i + " of method " + method.getName());
					} else if (candidates.size() > 1) {
						throw new Exception("expected single matching bean but found 2");
					}
					argsForMethod[i] = candidates.get(0);
				}
				Object bean = method.invoke(config, argsForMethod);
				beans.put(method.getName(), bean);
			}
		}

		System.out.println(beans);

		for (Object bean : beans.values()) {
			for (java.lang.reflect.Field field : bean.getClass().getDeclaredFields()) {
				if (field.isAnnotationPresent(Autowired.class)) {
					Class<?> fieldType = field.getType();
					// injection de référence vers le bean de type fieldType
				} else if (field.isAnnotationPresent(Value.class)) {
					String propName = field.getAnnotation(Value.class).value();
					// injection de valeur
				}
			}
		}
	}
}
