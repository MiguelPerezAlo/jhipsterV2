'use strict';

angular.module('jhipsterApp')
    .controller('EstadioController', function ($scope, Estadio) {
        $scope.estadios = [];
        $scope.loadAll = function() {
            Estadio.query(function(result) {
               $scope.estadios = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Estadio.get({id: id}, function(result) {
                $scope.estadio = result;
                $('#deleteEstadioConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Estadio.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteEstadioConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.estadio = {nombre: null, creacion: null, aforo: null, id: null};
        };
    });
