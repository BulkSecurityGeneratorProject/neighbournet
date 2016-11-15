(function () {
    'use strict';
    angular
        .module('neighbournetApiApp')
        .factory('Document', Document);

    Document.$inject = ['$resource', 'DateUtils'];

    function Document($resource, DateUtils) {
        var resourceUrl = 'api/documents/:id';

        return $resource(resourceUrl, {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.created = DateUtils.convertLocalDateFromServer(data.created);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.created = DateUtils.convertLocalDateToServer(copy.created);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.created = DateUtils.convertLocalDateToServer(copy.created);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
