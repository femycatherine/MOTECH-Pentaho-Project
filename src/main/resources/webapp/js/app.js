(function () {
    'use strict';

    /* App Module */

    angular.module('pentaho', ['motech-dashboard', 'ngCookies', 'bootstrap', 'settingsService', 'transformationsService']).config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.
                when('/settings', {templateUrl: '../pentaho/partials/settings.html', controller: 'SettingsCtrl' }).
                when('/transformations', {templateUrl: '../pentaho/partials/transformations.html', controller: 'TransformationsCtrl'}).
                when('/createTrans', {templateUrl: '../pentaho/partials/createTransformation.html', controller: 'TransformationsCtrl'}).
                otherwise({redirectTo: '/settings'});
    }]);
}());
