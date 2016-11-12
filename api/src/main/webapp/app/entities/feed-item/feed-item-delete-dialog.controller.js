(function () {
    'use strict';

    angular
        .module('neighbournetApiApp')
        .controller('FeedItemDeleteController', FeedItemDeleteController);

    FeedItemDeleteController.$inject = ['$uibModalInstance', 'entity', 'FeedItem'];

    function FeedItemDeleteController($uibModalInstance, entity, FeedItem) {
        var vm = this;

        vm.feedItem = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete(id) {
            FeedItem.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
