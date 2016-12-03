(function() {
    'use strict';

    angular
        .module('neighbournetApiApp')
        .controller('AttributeDeleteController',AttributeDeleteController);

    AttributeDeleteController.$inject = ['$uibModalInstance', 'entity', 'Attribute'];

    function AttributeDeleteController($uibModalInstance, entity, Attribute) {
        var vm = this;

        vm.attribute = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Attribute.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
