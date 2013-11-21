(function() {
    'use strict';

    /* Services */

    angular.module('settingsService', [ 'ngResource' ]).factory('Settings', function($resource) {
        return $resource('../pentaho/api/settings');
    });

    angular.module('transformationsService', [ 'ngResource' ]).factory('Transformations', function($resource) {
        return $resource('../pentaho/api/transformations', {}, {

            scheduleTrans : {
                method : 'PUT',
                params : {
                    immediate : false
                }
            },
            immediateTrans : {
                method : 'PUT',
                params : {
                    immediate : true
                }
            },
            deleteTrans : {
                method : 'DELETE',
                params : {
                    transId : 'transId'
                }
            }
        });
    });
}());
