'use strict';

angular.module('jhipsterApp')
    .controller('EntrenadorController', function ($scope, Entrenador) {
        $scope.entrenadors = [];
        $scope.loadAll = function() {
            Entrenador.query(function(result) {
               $scope.entrenadors = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Entrenador.get({id: id}, function(result) {
                $scope.entrenador = result;
                $('#deleteEntrenadorConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Entrenador.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteEntrenadorConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.entrenador = {nombre: null, nacimiento: null, nacionalidad: null, partidos: null, id: null};
        };
    });
