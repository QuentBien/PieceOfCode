module.exports = function (grunt) {
    require('load-grunt-tasks')(grunt);

    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-usemin');
    grunt.loadNpmTasks('grunt-bower-concat');
    grunt.loadNpmTasks('grunt-injector');
    grunt.loadNpmTasks('grunt-ng-annotate');
    grunt.loadNpmTasks('grunt-angular-templates');
    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-postcss');
    grunt.loadNpmTasks('grunt-assets-versioning');
    grunt.loadNpmTasks('grunt-contrib-clean');

    // Project configuration.
    grunt.initConfig({

        //properties from package.json
        pkg: grunt.file.readJSON('package.json'),

        // Compiles Sass to CSS and generates necessary files if requested
        sass: {
            options: {
                sourceMap: true
            },
            dist: {
                files: [{
                    '<%= pkg.front_path %>/style.css': '<%= pkg.front_path %>/style.scss'
                }]
            }
        },

        // Get all external libraries sources and concat them
        bower_concat: {
            all: {
                dest: {
                    js: '<%= pkg.front_path %>/build/libs.js',
                    css: '<%= pkg.front_path %>/build/libs.css'
                },
                exclude: [
                    'angular-i18n',
                    'opentok-angular'
                ],
                mainFiles: {
                    'sass-mixins': ['mixins.scss'],
                    'momentjs': ['locale/fr.js', 'moment.js'],
                    'intl-tel-input': [
                        'build/js/intlTelInput.js',
                        'build/js/utils.js',
                        'build/css/intlTelInput.css'
                    ]
                },
                bowerOptions: {
                    relative: true
                },
                dependencies: {
                    'angular': 'jquery' // load jQuery before angular, then angular will use jQuery
                }
            }
        },

        // Includes all sources in the entry point
        injector: {
            options: {
                destFile: '<%= pkg.front_path %>/index.html',
                ignorePath: '<%= pkg.front_path %>',
                addRootSlash: false
            },
            dev: {
                files: {
                    src: ['<%= pkg.front_path %>/build/libs.js',
                        '<%= pkg.front_path %>/pieceOfCode.js',
                        '<%= pkg.front_path %>/**/*.js',
                        '!<%= pkg.front_path %>/build/sources.js',
                        '!<%= pkg.front_path %>/build/templates.js',
                        '!<%= pkg.front_path %>/assets/**/*.js',
                        '!<%= pkg.front_path %>/libraries/**/*.js',
                        '<%= pkg.front_path %>/build/libs.css',
                        '<%= pkg.front_path %>/style.css']
                }
            },
            prod: {
                files: {
                    src: ['<%= pkg.front_path %>/build/libs.*.js',
                        '<%= pkg.front_path %>/build/sources.*.js',
                        '<%= pkg.front_path %>/build/templates.*.js',
                        '<%= pkg.front_path %>/build/libs.*.css',
                        '<%= pkg.front_path %>/style.*.css']
                }
            }
        },

        clean: {
            all: ['<%= pkg.front_path %>/build/*', '<%= pkg.front_path %>/style.*.css'],
            tmpFiles: ['<%= pkg.front_path %>/build/libs.js',
                '<%= pkg.front_path %>/build/sources.js',
                '<%= pkg.front_path %>/build/templates.js',
                '<%= pkg.front_path %>/build/libs.css',
                '<%= pkg.front_path %>/style.css']
        },

        assets_versioning: {
            options: {
                tag: 'hash',
                hashLength: 6
            },
            prod: {
                files: {
                    '<%= pkg.front_path %>/build/libs.js': '<%= pkg.front_path %>/build/libs.js',
                    '<%= pkg.front_path %>/build/sources.js': '<%= pkg.front_path %>/build/sources.js',
                    '<%= pkg.front_path %>/build/templates.js': '<%= pkg.front_path %>/build/templates.js',
                    '<%= pkg.front_path %>/build/libs.css': '<%= pkg.front_path %>/build/libs.css',
                    '<%= pkg.front_path %>/style.css': '<%= pkg.front_path %>/style.css'
                }
            }
        },

        // Concat all sources files
        concat: {
            dist: {
                src: [
                    '<%= pkg.front_path %>/pieceOfCode.js',
                    '<%= pkg.front_path %>/**/*.js',
                    '!<%= pkg.front_path %>/build/**/*.js',
                    '<%= pkg.front_path %>/build/templates.js',
                    '!<%= pkg.front_path %>/assets/**/*.js',
                    '!<%= pkg.front_path %>/libraries/**/*.js'
                ],
                dest: '<%= pkg.front_path %>/build/sources.js'
            }
        },

        // Check angular file dependencies
        ngAnnotate: {
            pieceOfCode: {
                files: {
                    '<%= pkg.front_path %>/build/sources.js': ['<%= pkg.front_path %>/build/sources.js']
                }
            }
        },
        ngtemplates: {
            pieceOfCode: {
                cwd: '<%= pkg.front_path %>',
                src: [
                    '**/*.html',
                    '!build/**/*.html',
                    '!libraries/**/*.html',
                    '!assets/**/*.html',
                    '!index.html'
                ],
                dest: '<%= pkg.front_path %>/build/templates.js'
            }
        },

        // Code compression
        uglify: {
            build: {
                files: {
                    '<%= pkg.front_path %>/build/libs.js': ['<%= pkg.front_path %>/build/libs.js'],
                    '<%= pkg.front_path %>/build/sources.js': ['<%= pkg.front_path %>/build/sources.js']
                }
            }
        },

        // Watch sass files and reload the css when needed
        watch: {
            scripts: {
                files: ['<%= pkg.front_path %>/**/*.scss'],
                tasks: ['sass'],
                options: {
                    spawn: false
                }
            }
        },

        postcss: {
            options: {
                map: true, // inline sourcemaps

                processors: [
                    require('autoprefixer')({browsers: 'last 2 versions'}) // add vendor prefixes
                ]
            },
            dist: {
                src: ['<%= pkg.front_path %>/style.css', '<%= pkg.front_path %>/build/libs.css']
            }
        }
    });

    // Production tasks
    grunt.registerTask('prod', [
        'clean:all',
        'sass',
        'bower_concat',
        'ngtemplates',
        'concat',
        'postcss',
        'ngAnnotate',
        'uglify',
        'assets_versioning',
        'clean:tmpFiles',
        'injector:prod'
    ]);

    // Development tasks
    grunt.registerTask('dev', [
        'clean:all',
        'sass',
        'bower_concat',
        'postcss',
        'injector:dev',
        'watch'
    ]);

    grunt.registerTask('default', [
        'dev'
    ]);
};