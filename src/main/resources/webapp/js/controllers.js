(function() {
    'use strict';

    /* Controllers */

    var pentahoModule = angular.module('pentaho');

    pentahoModule.controller('SettingsCtrl', function($scope, Settings) {
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

    pentahoModule.controller('TransformationsCtrl', function($scope, Transformations, $http) {

        $scope.transformation = {};

        $scope.addParam = function() {
            if (!$scope.transformation.params) {
                $scope.transformation.params = [];
            }

            $scope.transformation.params.push({});
        };

        $scope.removeParam = function(index) {
            $scope.transformation.params.splice(index, 1);
        }

        $scope.createNewTrans = function(trans) {
            $http.post('../pentaho/api/transformations', $scope.transformation).success(function() {
                motechAlert('pentaho.transformations.success.saved', 'server.saved');
                $scope.transformation = {};
            }).error(function() {
                motechAlert('pentaho.transformations.error.saved', 'server.error');
            });
        };

        $scope.transformations = Transformations.query(function() {
            $scope.transformationError = false;
            unblockUI();
        }, function() {
            $scope.transformationError = true;
            unblockUI();
        });

        $scope.submit = function(trans) {
            trans.$scheduleTrans(function() {
                motechAlert('pentaho.transformations.success.saved', 'server.saved');
            }, function() {
                motechAlert('pentaho.transformations.error.saved', 'server.error');
            });
        };

        $scope.runImmediately = function(trans) {
            trans.$immediateTrans(function() {
                motechAlert('pentaho.transformations.success.immediate', 'server.saved');
            }, function() {
                motechAlert('pentaho.transformations.error.immediate', 'server.error');
            });
        };

        $scope.removeTrans = function(index, trans) {

            trans.$deleteTrans({
                transId : trans._id
            }, function() {
                $scope.transformations.splice(index, 1);
                motechAlert('pentaho.transformations.success.removed', 'server.saved');
            }, function() {
                motechAlert('pentaho.transformations.error.removed', 'server.error');
            });
        };
    });
}());
