(function () {
    'use strict';
    angular
        .module('neighbournetApiApp')
        .factory('FeedItem', FeedItem);

    FeedItem.$inject = ['$resource'];

    function FeedItem($resource) {
        var resourceUrl = 'api/feed-items/:id';

        return $resource(resourceUrl, {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': {method: 'PUT'}
        });
    }
})();
