(function () {
    'use strict';

    /* Controllers */

    var pentahoModule = angular.module('pentaho');

    pentahoModule.controller('SettingsCtrl', function ($scope, Settings) {
        $scope.settings = Settings.get();

        $scope.submit = function() {
            $scope.settings.$save(function() {
                motechAlert('pentaho.settings.success.saved', 'server.saved');
            }, function() {
                motechAlert('pentaho.settings.error.saved', 'server.error');
            });
        };

        $scope.isNumeric = function(prop) {
            return $scope.settings.hasOwnProperty(prop) && /^[0-9]+$/.test($scope.settings[prop]);
        };

    });
    
    pentahoModule.controller('TransformationsCtrl', function ($scope, Transformations) {
    	
    	
    });

}());
