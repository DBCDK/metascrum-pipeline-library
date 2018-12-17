#!/usr/bin/env groovy

// Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
// See license text at https://opensource.dbc.dk/licenses/gpl-3.0

def call(String deployment_path, String kubecert, String namespace,
		steps) {
	steps.sh """#!/usr/bin/env bash
		set -xe
		DEPLOYMENT_NAME=\$(kubectl get -n ${namespace} -f ${deployment_path} -o name | grep deployment)
		kubectl --kubeconfig ${kubecert} apply -f ${deployment_path} -n ${namespace}
		kubectl rollout status deployment -n ${namespace} \$DEPLOYMENT_NAME
	"""
}
