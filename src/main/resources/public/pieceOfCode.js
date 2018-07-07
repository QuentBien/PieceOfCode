angular.module('pieceOfCode', [
    'ui.router',
    'ngMaterial',
    'ngLodash',
    'ngCookies',
    'ngResource',
    'ngMessages',
    'ngSanitize',
    'ngAnimate',
    'angularMoment',
    'dateParser',
    'material.components.expansionPanels',
    'angular-md5',
    'infinite-scroll',
    'md.data.table',
    'angularLoadingDots',
    'ui.codemirror'
]).run(function ($window, $rootScope, $resource, $location, $state, $log, moment) {
    $rootScope.$state = $state;
    $rootScope.$location = $location;
    $rootScope.textAreasMaxCharacters = 10000;
    $rootScope.mdToasterHideDelay = 8000;
    $rootScope.today = moment();
    $rootScope.isSafari = /^((?!chrome|android).)*safari/i.test(navigator.userAgent);
    $rootScope.isIE = $window.bowser.msie;
    $rootScope.isEdge = $window.bowser.msedge;
    $rootScope.browserSupported = ($window.bowser.msie && $window.bowser.version >= 11) || ($window.bowser.chrome && $window.bowser.version >= 62) || ($window.bowser.firefox && $window.bowser.version >= 57) || $window.bowser.msedge || $window.bowser.safari || $window.bowser.opera;
});