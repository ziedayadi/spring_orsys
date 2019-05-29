class MainController{
	
	constructor($http){
		this.authentication = null;
		this.$http = $http;
	}
	
	logout(){
		this.$http.post("http://localhost:8080/logout").then(x => this.authentication= null);

	}
	
	tryAuthenticate(username, password){
		const credentials = `${username}:${password}`
		var req = {
				 	method: 'POST',
				 	url: 'http://localhost:8080/authentication',
				 	headers: {	'Authorization':  `Basic ${btoa(credentials)}`}
		}
		
		this.$http(req).then(x => {
			if(x.status == 200) {
				this.authentication = x.data;
			}
		})
	}
}
class BookController{
	
	constructor($http){
		this.results = null;
		this.$http = $http;
	}
	
	find(title){
		const uri = `http://localhost:8080/books/search/byTitle?title=${title}`;
		this.$http.get(uri).then(x => this.results = x.data._embedded.books);
	}
}


angular.module('ex3-boot', [ 'ngRoute' ])
	.config($qProvider => $qProvider.errorOnUnhandledRejections(false))
	.config($routeProvider => {
		$routeProvider.when('/books', {templateUrl : 'partials/books.html'})
		$routeProvider.when('/books/:id', {templateUrl : 'partials/book.html'})
		.otherwise({redirectTo : '/'});
	}).controller("BookController", BookController)
	.controller("MainController", MainController);