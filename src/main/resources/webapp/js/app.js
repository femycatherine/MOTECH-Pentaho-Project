(function () {
    'use strict';

    /* App Module */

    angular.module('pentaho', ['motech-dashboard', 'ngCookies', 'bootstrap', 'settingsService']).config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.
                when('/settings', {templateUrl: '../pentaho/partials/settings.html', controller: 'SettingsCtrl' }).
                when('/transformations', {templateUrl: '../pentaho/partials/transformations.html', controller: 'SettingsCtrl'}).
                otherwise({redirectTo: '/settings'});
    }]);
}());
