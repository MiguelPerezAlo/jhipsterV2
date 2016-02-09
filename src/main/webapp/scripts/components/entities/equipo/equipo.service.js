'use strict';

angular.module('jhipsterApp')
    .factory('Equipo', function ($resource, DateUtils) {
        return $resource('api/equipos/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.creacion = DateUtils.convertLocaleDateFromServer(data.creacion);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.creacion = DateUtils.convertLocaleDateToServer(data.creacion);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.creacion = DateUtils.convertLocaleDateToServer(data.creacion);
                    return angular.toJson(data);
                }
            }
        });
    });
