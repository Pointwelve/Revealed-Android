# frozen_string_literal: true
source "https://rubygems.org"

gem 'danger', '~> 6'
gem "fastlane", '~> 2.122'

plugins_path = File.join(File.dirname(__FILE__), 'fastlane', 'Pluginfile')
eval_gemfile(plugins_path) if File.exist?(plugins_path)
