(function () {
	'use strict';

	/* Controllers */

	var pentahoModule = angular.module('pentaho');

	pentahoModule.controller('SettingsCtrl', function ($scope, Settings) {
		$scope.settings = Settings.get();

		$scope.transformation = {};

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
		$scope.transformations = Transformations.query(function() {
			$scope.transformationError = false;
			unblockUI();
		}, function() {
			$scope.transformationError = true;
			unblockUI();
		});

		$scope.submit = function(trans) {
			trans.$updateTrans(function() {
				motechAlert('pentaho.transformations.success.saved', 'server.saved');
			}, function() {
				motechAlert('pentaho.transformations.error.saved', 'server.error');
			});
		};

		$scope.removeTrans = function(trans) {

			trans.$deleteTrans({transId: trans._id}, function() {
				motechAlert('pentaho.transformations.success.removed', 'server.saved');
			}, function() {
				motechAlert('pentaho.transformations.error.removed', 'server.error');
			});
		};
	});
}());
