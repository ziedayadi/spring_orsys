# Remarque formation

## Day 1
 

- Spring (IoC)  
- JPA (Many to one, one to many, join by) 
- Hibernate 
- Transaction
- How to create an entity manager 
- How to create an entity manager factory 
- How each method declared transactional is called by a proxy and not the method itself. 
- Entity manager is alive only at the transaction, it is annotated `@PercistenceContext`  

    - Entity manager has methods:
    
        - find
    
            - T <T> find (Class<T> clazz), Object id) 
            - Optional<T> find (Class<T> clazz), Object id)     
        
        - Save 
            
            - If entity is transient (a new entity): `persist`
            - If entity is attached (a new entity): nothing to do
            - If entity is detached (a new entity): `merge`
            
        - Delete 
        - Query 
        
            - `New namedQuery()'


## Day 2

### Hibernae 

- Les Recherches par exemple 
- Si on fait un une modification sur une entité attaché, le save marchera sur la BD 
- Dans spring test, si le test est annoté par `@Transactional` 
- Fait attention aux Niveaux d'isolation des transactions (T1 modifie une entité, esq l'autre transaction peut modifier la meme entit, esq la T2 peut voir les modifications de T1 ? )
- deux niveaux de cach
    
    - *cache 1*:au niveaux de `l'EntityManeger`
    - *cache 2*:Au niveau de la `entityManagerFactory` 
    
- Spring-boot: NA
- MangoRepository au lieu de JPA repository 

### spring-data-rest

- Simple 
- `@RepositoryEventHandler and @HandleBeforeCreate ...` Are annotations on Rules Component to execute some code before saving

## Day 3

### spring-security 

- Une classe annoté par @EnableWebSecurity et hérite de WebSecurityConfigurerAdapter
- Overrider la méthode  'configure(AuthenticationManagerBuilder auth) ' Pour preciser la requette de user, l'algo de cryptage et les autoritées. 
- Overrider la méthode configure(HttpSecurity http)  les authorisations. 


### spring-batch 
- Notion de JOB et STEP 
- 2 types de tep: Chunk: orienté ligne et tasklet orienté job( exmpl envoie mail)

 





