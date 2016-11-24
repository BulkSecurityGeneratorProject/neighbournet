(function () {
    'use strict';

    angular
        .module('neighbournetApiApp')
        .controller('DocumentDetailController', DocumentDetailController);

    DocumentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Document', 'Site'];

    function DocumentDetailController($scope, $rootScope, $stateParams, previousState, entity, Document, Site) {
        var vm = this;

        vm.document = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('neighbournetApiApp:documentUpdate', function (event, result) {
            vm.document = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
