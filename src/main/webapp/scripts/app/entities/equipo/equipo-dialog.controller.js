'use strict';

angular.module('jhipsterApp').controller('EquipoDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Equipo', 'Jugador', 'Estadio', 'Socio', 'Entrenador', 'Temporada',
        function($scope, $stateParams, $modalInstance, entity, Equipo, Jugador, Estadio, Socio, Entrenador, Temporada) {

        $scope.equipo = entity;
        $scope.jugadors = Jugador.query();
        $scope.estadios = Estadio.query();
        $scope.socios = Socio.query();
        $scope.entrenadors = Entrenador.query();
        $scope.temporadas = Temporada.query();
        $scope.load = function(id) {
            Equipo.get({id : id}, function(result) {
                $scope.equipo = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('jhipsterApp:equipoUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.equipo.id != null) {
                Equipo.update($scope.equipo, onSaveFinished);
            } else {
                Equipo.save($scope.equipo, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
