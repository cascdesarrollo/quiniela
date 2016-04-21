angular.module('loginServices', [])
        .factory('factoryLoginService', function ($http) {
            return {
                validar: function (username, password) {
                    return $http({
                        method: 'POST',
                        url: QUINIELA + 'session/login',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded',
                        },
                        dataType: "json",
                        data: $.param({
                            'username': username,
                            'password': password
                        })
                    });
                },
                salir: function (token) {
                    return $http({
                        method: 'POST',
                        url: QUINIELA + 'session/logout',
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
