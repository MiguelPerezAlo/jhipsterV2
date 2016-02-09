'use strict';

angular.module('jhipsterApp')
    .controller('EstadioDetailController', function ($scope, $rootScope, $stateParams, entity, Estadio, Equipo) {
        $scope.estadio = entity;
        $scope.load = function (id) {
            Estadio.get({id: id}, function(result) {
                $scope.estadio = result;
            });
        };
        $rootScope.$on('jhipsterApp:estadioUpdate', function(event, result) {
            $scope.estadio = result;
        });
    });
