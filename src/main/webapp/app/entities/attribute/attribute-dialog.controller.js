(function() {
    'use strict';

    angular
        .module('neighbournetApiApp')
        .controller('AttributeDialogController', AttributeDialogController);

    AttributeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Attribute', 'Document', 'Selector'];

    function AttributeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Attribute, Document, Selector) {
        var vm = this;

        vm.attribute = entity;
        vm.clear = clear;
        vm.save = save;
        vm.documents = Document.query();
        vm.selectors = Selector.query();
        vm.attributes = Attribute.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.attribute.id !== null) {
                Attribute.update(vm.attribute, onSaveSuccess, onSaveError);
            } else {
                Attribute.save(vm.attribute, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('neighbournetApiApp:attributeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
