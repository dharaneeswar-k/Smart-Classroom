INSERT INTO attendance_policy (
    confidence_threshold,
    min_presence_percentage,
    slot_duration_minutes,
    late_entry_grace_minutes,
    early_exit_grace_minutes,
    late_entry_handling,
    early_exit_handling,
    manual_override_enabled,
    od_override_enabled,
    is_active,
    effective_from
) SELECT 
    0.60,
    60.0,
    10,
    5,
    5,
    'ALLOW',
    'ALLOW',
    true,
    true,
    true,
    NOW()
WHERE NOT EXISTS (SELECT 1 FROM attendance_policy);
