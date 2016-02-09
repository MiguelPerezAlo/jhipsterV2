'use strict';

angular.module('jhipsterApp').controller('JugadorDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Jugador', 'Equipo',
        function($scope, $stateParams, $modalInstance, entity, Jugador, Equipo) {

        $scope.jugador = entity;
        $scope.equipos = Equipo.query();
        $scope.load = function(id) {
            Jugador.get({id : id}, function(result) {
                $scope.jugador = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('jhipsterApp:jugadorUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.jugador.id != null) {
                Jugador.update($scope.jugador, onSaveFinished);
            } else {
                Jugador.save($scope.jugador, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
