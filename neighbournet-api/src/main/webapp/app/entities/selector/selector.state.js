(function () {
    'use strict';

    angular
        .module('neighbournetApiApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('selector', {
                parent: 'entity',
                url: '/selector',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Selectors'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/selector/selectors.html',
                        controller: 'SelectorController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {}
            })
            .state('selector-detail', {
                parent: 'entity',
                url: '/selector/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Selector'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/selector/selector-detail.html',
                        controller: 'SelectorDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Selector', function ($stateParams, Selector) {
                        return Selector.get({id: $stateParams.id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'selector',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('selector-detail.edit', {
                parent: 'selector-detail',
                url: '/detail/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/selector/selector-dialog.html',
                        controller: 'SelectorDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Selector', function (Selector) {
                                return Selector.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('^', {}, {reload: false});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('selector.new', {
                parent: 'selector',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/selector/selector-dialog.html',
                        controller: 'SelectorDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    value: null,
                                    name: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function () {
                        $state.go('selector', null, {reload: 'selector'});
                    }, function () {
                        $state.go('selector');
                    });
                }]
            })
            .state('selector.edit', {
                parent: 'selector',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/selector/selector-dialog.html',
                        controller: 'SelectorDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Selector', function (Selector) {
                                return Selector.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('selector', null, {reload: 'selector'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('selector.delete', {
                parent: 'selector',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/selector/selector-delete-dialog.html',
                        controller: 'SelectorDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['Selector', function (Selector) {
                                return Selector.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('selector', null, {reload: 'selector'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            });
    }

})();
