angular.module('registroServices', [])
        .factory('factoryRegistroService', function ($http) {
            return {
                registrar: function (nombre, telefono, email, password) {
                    return $http({
                        method: 'POST',
                        url: QUINIELA + 'usuarios',
                        dataType: "json",
                        data: {
                            'nombre': nombre,
                            'telefono': telefono,
                            'email': email,
                            'password': password
                        }
                    });
                },
                salir: function (token) {
                    return $http({
                        method: 'POST',
                        url: FRONTINOCLI + 'session/logout',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded',
                        },
                        dataType: "json",
                        data: $.param({
                            'valida': token
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
