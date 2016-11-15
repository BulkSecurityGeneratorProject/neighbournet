(function () {
    'use strict';

    angular
        .module('neighbournetApiApp')
        .controller('SiteDetailController', SiteDetailController);

    SiteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Site', 'Document', 'Selector'];

    function SiteDetailController($scope, $rootScope, $stateParams, previousState, entity, Site, Document, Selector) {
        var vm = this;

        vm.site = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('neighbournetApiApp:siteUpdate', function (event, result) {
            vm.site = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
