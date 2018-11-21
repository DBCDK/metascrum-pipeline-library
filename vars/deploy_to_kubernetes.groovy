#!/usr/bin/env groovy

// Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
// See license text at https://opensource.dbc.dk/licenses/gpl-3.0

def call(String deployment_path, String docker_tag, String kubecert, String namespace,
		Closure checkout_scm_body, steps, String template_keys_path = "") {
	String template_keys_string = ""
	if(template_keys_path != "") {
		template_keys_string = "--template-keys-file ${template_keys_path}"
	}
	checkout_scm_body()
	steps.sh """#!/usr/bin/env bash
		set -xe
		rm -rf ENV
		python3 -m venv ENV
		source ENV/bin/activate
		pip3 install -U pip
		pip3 install git+https://github.com/DBCDK/deployment-utils#egg=deploymentutils
		python -m deploymentutils.templater ${deployment_path} ${template_keys_string} --template-keys DOCKER_TAG=${docker_tag} | kubectl --kubeconfig ${kubecert} apply -f - -n ${namespace}
	"""
}
