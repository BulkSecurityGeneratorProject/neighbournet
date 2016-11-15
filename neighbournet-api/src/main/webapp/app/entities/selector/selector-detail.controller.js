(function () {
    'use strict';

    angular
        .module('neighbournetApiApp')
        .controller('SelectorDetailController', SelectorDetailController);

    SelectorDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Selector', 'Site'];

    function SelectorDetailController($scope, $rootScope, $stateParams, previousState, entity, Selector, Site) {
        var vm = this;

        vm.selector = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('neighbournetApiApp:selectorUpdate', function (event, result) {
            vm.selector = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
