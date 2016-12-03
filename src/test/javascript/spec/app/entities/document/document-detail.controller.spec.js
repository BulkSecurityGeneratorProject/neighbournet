'use strict';

describe('Controller Tests', function () {

    describe('Document Management Detail Controller', function () {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockDocument, MockSite, MockAttribute;
        var createController;

        beforeEach(inject(function ($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockDocument = jasmine.createSpy('MockDocument');
            MockSite = jasmine.createSpy('MockSite');
            MockAttribute = jasmine.createSpy('MockAttribute');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Document': MockDocument,
                'Site': MockSite,
                'Attribute': MockAttribute
            };
            createController = function () {
                $injector.get('$controller')("DocumentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function () {
            it('Unregisters root scope listener upon scope destruction', function () {
                var eventType = 'neighbournetApiApp:documentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
