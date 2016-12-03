(function() {
    'use strict';

    angular
        .module('neighbournetApiApp')
        .controller('SelectorDialogController', SelectorDialogController);

    SelectorDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Selector', 'Site'];

    function SelectorDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Selector, Site) {
        var vm = this;

        vm.selector = entity;
        vm.clear = clear;
        vm.save = save;
        vm.sites = Site.query();
        vm.selectors = Selector.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.selector.id !== null) {
                Selector.update(vm.selector, onSaveSuccess, onSaveError);
            } else {
                Selector.save(vm.selector, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('neighbournetApiApp:selectorUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
