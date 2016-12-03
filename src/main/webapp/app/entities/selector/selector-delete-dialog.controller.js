(function() {
    'use strict';

    angular
        .module('neighbournetApiApp')
        .controller('SelectorDeleteController',SelectorDeleteController);

    SelectorDeleteController.$inject = ['$uibModalInstance', 'entity', 'Selector'];

    function SelectorDeleteController($uibModalInstance, entity, Selector) {
        var vm = this;

        vm.selector = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Selector.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
