#!/usr/bin/env groovy

// Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
// See license text at https://opensource.dbc.dk/licenses/gpl-3.0

def call(String deployment_path, String kubecert, String namespace,
		steps) {
	steps.sh """#!/usr/bin/env bash
		set -xe
		KUBECTL=\"kubectl --kubeconfig ${kubecert} -n ${namespace}\"
		DEPLOYMENT_NAME=\$(\$KUBECTL get -f ${deployment_path} -o name | grep deployment)
		\$KUBECTL apply -f ${deployment_path}
		\$KUBECTL rollout status \$DEPLOYMENT_NAME
	"""
}
