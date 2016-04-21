angular.module('agregarServices', [])
        .factory('factoryAgregarService', function ($http) {
            return {
                partidos: function (token) {
                    return $http({
                        method: 'GET',
                        url: QUINIELA + 'quinielas/nuevadetalle?valida='+token
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
