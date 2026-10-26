#!/bin/sh
# =============================================================================
# vault-init.sh – Seeds initial secrets into Vault dev server.
# =============================================================================

set -eu

: "${VAULT_ADDR:=http://localhost:8200}"
: "${VAULT_TOKEN:?Set VAULT_TOKEN}"
: "${ARISPAY_DB_PASSWORD:?Set ARISPAY_DB_PASSWORD}"
: "${ARISPAY_DB_PASSWORD_PROD:?Set ARISPAY_DB_PASSWORD_PROD}"
: "${ARISPAY_JWT_SECRET:?Set ARISPAY_JWT_SECRET}"
: "${ARISPAY_JWT_SECRET_PROD:?Set ARISPAY_JWT_SECRET_PROD}"
: "${ARISPAY_REDIS_PASSWORD:?Set ARISPAY_REDIS_PASSWORD}"
: "${ARISPAY_REDIS_PASSWORD_PROD:?Set ARISPAY_REDIS_PASSWORD_PROD}"
: "${EUREKA_SHARED_SECRET_DEV:?Set EUREKA_SHARED_SECRET_DEV}"
: "${EUREKA_SHARED_SECRET_PROD:?Set EUREKA_SHARED_SECRET_PROD}"
: "${ZIPKIN_TOKEN_DEV:?Set ZIPKIN_TOKEN_DEV}"
: "${ZIPKIN_TOKEN_PROD:?Set ZIPKIN_TOKEN_PROD}"
: "${PAYMENT_GATEWAY_API_KEY_DEV:?Set PAYMENT_GATEWAY_API_KEY_DEV}"
: "${PAYMENT_GATEWAY_API_KEY_PROD:?Set PAYMENT_GATEWAY_API_KEY_PROD}"

export VAULT_ADDR VAULT_TOKEN

echo ">>> Enabling KV v2 secrets engine at 'secret/'..."
vault secrets enable -path=secret kv-v2 2>/dev/null || echo "Already enabled"

echo ">>> Seeding gateway secrets (dev profile)..."
vault kv put secret/gateway/dev \
  eureka.password="${EUREKA_SHARED_SECRET_DEV}" \
  zipkin.token="${ZIPKIN_TOKEN_DEV}"

echo ">>> Seeding gateway secrets (prod profile)..."
vault kv put secret/gateway/prod \
  eureka.password="${EUREKA_SHARED_SECRET_PROD}" \
  zipkin.token="${ZIPKIN_TOKEN_PROD}"

echo ">>> Seeding arisPayApi application secrets (dev profile)..."
vault kv put secret/arisPayApi/dev \
  spring.datasource.password="${ARISPAY_DB_PASSWORD}" \
  spring.data.redis.password="${ARISPAY_REDIS_PASSWORD}" \
  custom.arispay.app.jwt-secret="${ARISPAY_JWT_SECRET}" \
  payment.gateway.api-key="${PAYMENT_GATEWAY_API_KEY_DEV}"

echo ">>> Seeding arisPayApi application secrets (prod profile)..."
vault kv put secret/arisPayApi/prod \
  spring.datasource.password="${ARISPAY_DB_PASSWORD_PROD}" \
  spring.data.redis.password="${ARISPAY_REDIS_PASSWORD_PROD}" \
  custom.arispay.app.jwt-secret="${ARISPAY_JWT_SECRET_PROD}" \
  payment.gateway.api-key="${PAYMENT_GATEWAY_API_KEY_PROD}"

echo ">>> Seeding global application defaults..."
vault kv put secret/application \
  management.endpoints.web.exposure.include="*"

echo ">>> Vault initialisation complete."
vault kv list secret/ || true
