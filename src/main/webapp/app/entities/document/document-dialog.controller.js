(function () {
    'use strict';

    angular
        .module('neighbournetApiApp')
        .controller('DocumentDialogController', DocumentDialogController);

    DocumentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Document', 'Site', 'Attribute'];

    function DocumentDialogController($timeout, $scope, $stateParams, $uibModalInstance, entity, Document, Site, Attribute) {
        var vm = this;

        vm.document = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.documents = Document.query();
        vm.sites = Site.query();
        vm.attributes = Attribute.query();

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function save() {
            vm.isSaving = true;
            if (vm.document.id !== null) {
                Document.update(vm.document, onSaveSuccess, onSaveError);
            } else {
                Document.save(vm.document, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            $scope.$emit('neighbournetApiApp:documentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.created = false;

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
