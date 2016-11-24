(function () {
    'use strict';

    angular
        .module('neighbournetApiApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('attribute', {
                parent: 'entity',
                url: '/attribute?page&sort&search',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Attributes'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/attribute/attributes.html',
                        controller: 'AttributeController',
                        controllerAs: 'vm'
                    }
                },
                params: {
                    page: {
                        value: '1',
                        squash: true
                    },
                    sort: {
                        value: 'id,asc',
                        squash: true
                    },
                    search: null
                },
                resolve: {
                    pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                        return {
                            page: PaginationUtil.parsePage($stateParams.page),
                            sort: $stateParams.sort,
                            predicate: PaginationUtil.parsePredicate($stateParams.sort),
                            ascending: PaginationUtil.parseAscending($stateParams.sort),
                            search: $stateParams.search
                        };
                    }]
                }
            })
            .state('attribute-detail', {
                parent: 'entity',
                url: '/attribute/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Attribute'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/attribute/attribute-detail.html',
                        controller: 'AttributeDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Attribute', function ($stateParams, Attribute) {
                        return Attribute.get({id: $stateParams.id}).$promise;
                    }],
                    previousState: ["$state", function ($state) {
                        var currentStateData = {
                            name: $state.current.name || 'attribute',
                            params: $state.params,
                            url: $state.href($state.current.name, $state.params)
                        };
                        return currentStateData;
                    }]
                }
            })
            .state('attribute-detail.edit', {
                parent: 'attribute-detail',
                url: '/detail/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/attribute/attribute-dialog.html',
                        controller: 'AttributeDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Attribute', function (Attribute) {
                                return Attribute.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('^', {}, {reload: false});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('attribute.new', {
                parent: 'attribute',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/attribute/attribute-dialog.html',
                        controller: 'AttributeDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    value: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function () {
                        $state.go('attribute', null, {reload: 'attribute'});
                    }, function () {
                        $state.go('attribute');
                    });
                }]
            })
            .state('attribute.edit', {
                parent: 'attribute',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/attribute/attribute-dialog.html',
                        controller: 'AttributeDialogController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: ['Attribute', function (Attribute) {
                                return Attribute.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('attribute', null, {reload: 'attribute'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            })
            .state('attribute.delete', {
                parent: 'attribute',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/attribute/attribute-delete-dialog.html',
                        controller: 'AttributeDeleteController',
                        controllerAs: 'vm',
                        size: 'md',
                        resolve: {
                            entity: ['Attribute', function (Attribute) {
                                return Attribute.get({id: $stateParams.id}).$promise;
                            }]
                        }
                    }).result.then(function () {
                        $state.go('attribute', null, {reload: 'attribute'});
                    }, function () {
                        $state.go('^');
                    });
                }]
            });
    }

})();
