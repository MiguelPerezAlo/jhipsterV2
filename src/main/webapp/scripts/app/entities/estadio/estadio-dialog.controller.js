'use strict';

angular.module('jhipsterApp').controller('EstadioDialogController',
    ['$scope', '$stateParams', '$modalInstance', '$q', 'entity', 'Estadio', 'Equipo',
        function($scope, $stateParams, $modalInstance, $q, entity, Estadio, Equipo) {

        $scope.estadio = entity;
        $scope.equipos = Equipo.query({filter: 'estadio-is-null'});
        $q.all([$scope.estadio.$promise, $scope.equipos.$promise]).then(function() {
            if (!$scope.estadio.equipo.id) {
                return $q.reject();
            }
            return Equipo.get({id : $scope.estadio.equipo.id}).$promise;
        }).then(function(equipo) {
            $scope.equipos.push(equipo);
        });
        $scope.load = function(id) {
            Estadio.get({id : id}, function(result) {
                $scope.estadio = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('jhipsterApp:estadioUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.estadio.id != null) {
                Estadio.update($scope.estadio, onSaveFinished);
            } else {
                Estadio.save($scope.estadio, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
