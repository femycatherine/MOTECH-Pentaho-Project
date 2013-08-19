(function () {
    'use strict';

    /* App Module */

    angular.module('pentaho', ['motech-dashboard', 'settingsServices', 'permissionsServices', 'modulesServices', 'connectionService', 'ngCookies', 'bootstrap']).config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.
                when('/settings', {templateUrl: '../pentaho/resources/partials/settings.html', controller: 'SettingsCtrl' }).
                otherwise({redirectTo: '/settings'});
    }]);
}());
