'use strict';

angular.module('jhipsterApp')
    .factory('Socio', function ($resource, DateUtils) {
        return $resource('api/socios/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.nacimiento = DateUtils.convertLocaleDateFromServer(data.nacimiento);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.nacimiento = DateUtils.convertLocaleDateToServer(data.nacimiento);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.nacimiento = DateUtils.convertLocaleDateToServer(data.nacimiento);
                    return angular.toJson(data);
                }
            }
        });
    });
