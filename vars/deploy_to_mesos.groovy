#!/usr/bin/env groovy

// Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
// See license text at https://opensource.dbc.dk/licenses/gpl-3.0

def call(String deployment_name, String docker_tag, String marathon_token,
		Closure checkout_scm_body, steps) {
	checkout_scm_body()
	// library-defined steps cannot call pipeline steps directly:
	// https://jenkins.io/doc/book/pipeline/shared-libraries/
	steps.sh """#!/usr/bin/env bash
		set -xe
		rm -rf ENV
		python3 -m venv ENV
		source ENV/bin/activate
		pip3 install --upgrade pip
		pip install -U -e \"git+https://github.com/DBCDK/mesos-tools.git#egg=mesos-tools\"

		python3 -m mesos_tools.marathon_config_producer ${deployment_name} --root marathon --template-keys DOCKER_TAG=${docker_tag} -o ${deployment_name}.json
		python3 -m mesos_tools.marathon_deployer -a ${marathon_token} -b https://mcp1.dbc.dk:8443 deploy ${deployment_name}.json
	"""
}
