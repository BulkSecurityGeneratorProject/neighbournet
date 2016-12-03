(function() {
    'use strict';
    angular
        .module('neighbournetApiApp')
        .factory('Attribute', Attribute);

    Attribute.$inject = ['$resource'];

    function Attribute ($resource) {
        var resourceUrl =  'api/attributes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
