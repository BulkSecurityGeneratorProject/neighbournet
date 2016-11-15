'use strict';

describe('Controller Tests', function () {

    describe('Attribute Management Detail Controller', function () {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockAttribute, MockDocument, MockSelector;
        var createController;

        beforeEach(inject(function ($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockAttribute = jasmine.createSpy('MockAttribute');
            MockDocument = jasmine.createSpy('MockDocument');
            MockSelector = jasmine.createSpy('MockSelector');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Attribute': MockAttribute,
                'Document': MockDocument,
                'Selector': MockSelector
            };
            createController = function () {
                $injector.get('$controller')("AttributeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function () {
            it('Unregisters root scope listener upon scope destruction', function () {
                var eventType = 'neighbournetApiApp:attributeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
