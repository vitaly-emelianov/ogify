module.exports = function (grunt) {
    grunt.initConfig({

        // define source files and their destinations
        uglify: {
            ogify: {
                options: {
                    beautify: true,
                    mangle: false
                },
                files: [{
                    src: ['libs/js/main.js', 'libs/js/services.js', 'libs/js/waiting-dialog.js', 'libs/js/ogify-api.js',
                        'libs/js/ogify-app.js', 'libs/js/controllers/dashboard-controller.js',
                        'libs/js/ogify-user-profile.js', 'libs/js/controllers/my-orders-controller.js'],
                    dest: 'jsm/ogify.min.js'
                }]
            },
            libs: {
                options: {
                    mangle: false,
                    compress: false,
                    beautify: false
                },
                files: [{
                    src: [
                        'node_modules/bootstrap/dist/js/bootstrap.min.js',
                        'node_modules/lodash/index.js',
                        'node_modules/angular/angular.min.js',
                        'node_modules/angular-sanitize/angular-sanitize.min.js',
                        'node_modules/angular-i18n/angular-locale_ru-ru.js',
                        'node_modules/angular-route/angular-route.min.js',
                        'node_modules/angular-resource/angular-resource.js',
                        'node_modules/angular-cookies/angular-cookies.js',
                        'node_modules/angular-google-maps/node_modules/angular-simple-logger/dist/angular-simple-logger.min.js',
                        'node_modules/angular-google-maps/dist/angular-google-maps.min.js',
                        'node_modules/bootstrap-datepicker/dist/js/bootstrap-datepicker.min.js',
                        'node_modules/bootstrap-datepicker/dist/locales/bootstrap-datepicker.ru.min.js',
                        'node_modules/clockpicker/dist/bootstrap-clockpicker.min.js',
                        'node_modules/angulartics/dist/angulartics.min.js',
                        'node_modules/angulartics-google-analytics/dist/angulartics-google-analytics.min.js',
                        'node_modules/intl-tel-input/lib/libphonenumber/build/utils.js',
                        'node_modules/intl-tel-input/build/js/intlTelInput.min.js'
                    ],
                    dest: 'jsm/ogify-libs.min.js'
                }]
            }
        },
        cssmin: {
            combine: {
                files: [{
                    src: [
                        'libs/css/animations.css',
                        'libs/css/ogify-main.css'
                    ],
                    dest: 'jsm/ogify-main.css'
                }, {
                    src: [
                        'node_modules/bootstrap/dist/css/bootstrap.min.css',
                        'node_modules/bootstrap-social/node_modules/font-awesome/css/font-awesome.css',
                        'node_modules/bootstrap-social/bootstrap-social.css',
                        'node_modules/bootstrap-datepicker/dist/css/bootstrap-datepicker3.min.css',
                        'node_modules/clockpicker/dist/bootstrap-clockpicker.min.css',
                        'node_modules/intl-tel-input/build/css/intlTelInput.css'
                    ],
                    dest: 'jsm/ogify-libs.css'
                }]
            }
        },
        watch: {
            js:  { files: 'libs/js/**.js', tasks: [ 'uglify' ] },
        }
    });

// load plugins
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-cssmin');

// register at least this one task
    grunt.registerTask('default', [ 'uglify', 'cssmin']);


};