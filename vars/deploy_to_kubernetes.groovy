#!/usr/bin/env groovy

// Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
// See license text at https://opensource.dbc.dk/licenses/gpl-3.0

def call(String deployment_path, String kubecert, String namespace,
		steps) {
	steps.sh """#!/usr/bin/env bash
		set -xe
		kubectl --kubeconfig ${kubecert} apply -f ${deployment_path} -n ${namespace}
	"""
}
