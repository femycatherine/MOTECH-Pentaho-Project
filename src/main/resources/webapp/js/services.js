(function () {
	'use strict';

	/* Services */

	angular.module('settingsService', ['ngResource']).factory('Settings', function($resource) {
		return $resource('../pentaho/api/settings');
	});

	angular.module('transformationsService', ['ngResource']).factory('Transformations', function($resource) {
		return $resource('../pentaho/api/transformations', {}, {

			updateTrans: {
				method: 'PUT',
				params: {
				}
			},
			deleteTrans: {
				method: 'DELETE',
				params: {
					transId: 'transId'
				}
			}
		});
	});
}());
