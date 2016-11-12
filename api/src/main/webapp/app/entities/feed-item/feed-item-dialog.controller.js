(function () {
    'use strict';

    angular
        .module('neighbournetApiApp')
        .controller('FeedItemDialogController', FeedItemDialogController);

    FeedItemDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'FeedItem'];

    function FeedItemDialogController($timeout, $scope, $stateParams, $uibModalInstance, entity, FeedItem) {
        var vm = this;

        vm.feedItem = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function save() {
            vm.isSaving = true;
            if (vm.feedItem.id !== null) {
                FeedItem.update(vm.feedItem, onSaveSuccess, onSaveError);
            } else {
                FeedItem.save(vm.feedItem, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            $scope.$emit('neighbournetApiApp:feedItemUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }


    }
})();
