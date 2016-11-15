(function () {
    'use strict';

    angular
        .module('neighbournetApiApp')
        .controller('SiteController', SiteController);

    SiteController.$inject = ['$scope', '$state', 'Site'];

    function SiteController($scope, $state, Site) {
        var vm = this;

        vm.sites = [];

        loadAll();

        function loadAll() {
            Site.query(function (result) {
                vm.sites = result;
            });
        }
    }
})();
