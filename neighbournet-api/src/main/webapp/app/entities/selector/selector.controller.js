(function () {
    'use strict';

    angular
        .module('neighbournetApiApp')
        .controller('SelectorController', SelectorController);

    SelectorController.$inject = ['$scope', '$state', 'Selector'];

    function SelectorController($scope, $state, Selector) {
        var vm = this;

        vm.selectors = [];

        loadAll();

        function loadAll() {
            Selector.query(function (result) {
                vm.selectors = result;
            });
        }
    }
})();
