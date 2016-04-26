angular.module('partidosServices', [])
        .factory('factoryPartidosService', function ($http) {
            return {
                partidos: function (token) {
                    return $http({
                        method: 'GET',
                        url: QUINIELA + 'partidos/pendientes?valida=' + token
                    });
                },
                guardar: function (token, username, password, id, equipo1, equipo2) {
                    return $http({
                        method: 'POST',
                        url: QUINIELA + 'partidos/actualizar?valida=' + token,
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        dataType: "json",
                        data: $.param({
                            'username': username,
                            'password': password,
                            'id': id,
                            'equipo1': equipo1,
                            'equipo2': equipo2
                        })
                    });
                }
            };
        })
        .factory('translationService', function ($resource) {
            return{
                getTranslation: function ($scope, language) {
                    var languageFilePath = 'translations/translation_' + language + '.json';
                    $resource(languageFilePath).get(function (data) {
                        $scope.translation = data;
                    });
                }};
        });
