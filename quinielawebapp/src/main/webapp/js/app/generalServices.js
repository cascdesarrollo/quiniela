angular.module('generalServices', [])
        .factory('factoryGeneralService', function ($http) {
            return {
                salir: function (token) {
                    return $http({
                        method: 'POST',
                        url: QUINIELA + 'session/logout',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        dataType: "json",
                        data: $.param({
                            'valida': token
                        })
                    });
                },
                datos: function (token) {
                    return $http({
                        method: 'GET',
                        url: QUINIELA + 'session/dataSession?valida='+token
                    });
                },
                tabla: function ( s) {
                    return $http({
                        method: 'GET',
                        url: QUINIELA + 'quinielas/consultar?s='+s
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
