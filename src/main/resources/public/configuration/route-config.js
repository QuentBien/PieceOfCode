angular.module('pieceOfCode').config(function ($locationProvider, $compileProvider, $qProvider, $stateProvider, $urlRouterProvider) {

    $locationProvider.hashPrefix('');
    $compileProvider.preAssignBindingsEnabled(true);
    $qProvider.errorOnUnhandledRejections(false);

    $urlRouterProvider.otherwise('/');

    var getData = function (object) {
        return object.data;
    };

    $stateProvider.state('authenticated', {
        abstract: true,
        resolve: {
            user: function ($state, $location, $q, UserService) {
                return UserService.getUser().$promise.catch(function () {
                    $state.go('login', {redirect: encodeURIComponent($location.url())}, {replace: true});
                    return $q.reject('You are not authenticated');
                });
            }
        },
        views: {
            'header@': {templateUrl: 'header/authenticated-header.html', controller: 'HeaderController as $ctrl'}
        }
    }).state('login', {
        url: '/login?redirect',
        views: {
            'header@': {templateUrl: 'header/visitor-header.html'},
            '@': {templateUrl: 'account/login.html', controller: 'LoginController as $ctrl'}
        },
        resolve: {
            credentials: function ($stateParams, $location, $q, UserService) {
                return UserService.user.$promise.then(function () {
                    return $q.reject("User is already logged in");
                }).catch(function () {
                    return {email: '', password: ''};
                });
            }
        }
    }).state('home', {
        parent: 'authenticated',
        url: '/?search&codeLanguage',
        views: {
            '@': {templateUrl: 'home/home.html', controller: 'HomeController as $ctrl'}
        }
    }).state('post', {
        parent: 'authenticated',
        url: '/post',
        views: {
            '@': {templateUrl: 'pieceOfCode/post.html', controller: 'PostController as $ctrl'}
        }
    }).state('pieceOfCode', {
        parent: 'authenticated',
        url: '/pieces-of-code/:pieceOfCodeId',
        views: {
            '@': {templateUrl: 'pieceOfCode/piece-of-code.html', controller: 'PieceOfCodeController as $ctrl'}
        },
        resolve: {
            pieceOfCode: function ($http, $stateParams, $state) {
                return $http.get("/pieces-of-code/" + $stateParams.pieceOfCodeId).then(getData)
                    .catch(function () {
                        $state.go("home");
                    });
            }
        }
    });
});