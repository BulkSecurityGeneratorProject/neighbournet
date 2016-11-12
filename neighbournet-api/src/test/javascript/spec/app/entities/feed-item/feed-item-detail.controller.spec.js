'use strict';

describe('Controller Tests', function () {

    describe('FeedItem Management Detail Controller', function () {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockFeedItem;
        var createController;

        beforeEach(inject(function ($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockFeedItem = jasmine.createSpy('MockFeedItem');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'FeedItem': MockFeedItem
            };
            createController = function () {
                $injector.get('$controller')("FeedItemDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function () {
            it('Unregisters root scope listener upon scope destruction', function () {
                var eventType = 'neighbournetApiApp:feedItemUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
