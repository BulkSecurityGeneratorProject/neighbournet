(function() {
    'use strict';

    angular
        .module('neighbournetApiApp')
        .controller('AttributeDetailController', AttributeDetailController);

    AttributeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Attribute', 'Document', 'Selector'];

    function AttributeDetailController($scope, $rootScope, $stateParams, previousState, entity, Attribute, Document, Selector) {
        var vm = this;

        vm.attribute = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('neighbournetApiApp:attributeUpdate', function(event, result) {
            vm.attribute = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
