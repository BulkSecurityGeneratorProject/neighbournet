(function () {
    'use strict';

    angular
        .module('neighbournetApiApp')
        .controller('FeedItemDetailController', FeedItemDetailController);

    FeedItemDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'FeedItem'];

    function FeedItemDetailController($scope, $rootScope, $stateParams, previousState, entity, FeedItem) {
        var vm = this;

        vm.feedItem = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('neighbournetApiApp:feedItemUpdate', function (event, result) {
            vm.feedItem = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
